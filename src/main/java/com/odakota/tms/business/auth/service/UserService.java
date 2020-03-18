package com.odakota.tms.business.auth.service;

import com.odakota.tms.business.auth.entity.User;
import com.odakota.tms.business.auth.entity.UserRole;
import com.odakota.tms.business.auth.repository.AccessTokenRepository;
import com.odakota.tms.business.auth.repository.UserRepository;
import com.odakota.tms.business.auth.repository.UserRoleRepository;
import com.odakota.tms.business.auth.resource.UserResource;
import com.odakota.tms.business.auth.resource.UserResource.UserCondition;
import com.odakota.tms.constant.Constant;
import com.odakota.tms.constant.FieldConstant;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.system.base.BaseParameter.FindCondition;
import com.odakota.tms.system.base.BaseService;
import com.odakota.tms.system.config.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class UserService extends BaseService<User, UserResource, UserCondition> {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AccessTokenRepository accessTokenRepository;

    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AccessTokenRepository accessTokenRepository,
                       UserRoleRepository userRoleRepository) {
        super(userRepository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenRepository = accessTokenRepository;
        this.userRoleRepository = userRoleRepository;
    }

    public User getUser(String username) {
        return userRepository.findByUsernameAndDeletedFlagFalse(username)
                             .orElseThrow(() -> new CustomException(MessageCode.MSG_INVALID_USERNAME_PASS,
                                                                    HttpStatus.BAD_REQUEST));
    }

    /**
     * Create a new resource.
     *
     * @param resource resource
     * @return The created resource is returned.
     */
    @Transactional
    @Override
    public UserResource createResource(UserResource resource) {
        // check duplicate username
        if (userRepository.isExistedResource(null, FieldConstant.USER_NAME, resource.getUsername())) {
            throw new CustomException(MessageCode.MSG_USER_NAME_EXISTED, HttpStatus.CONFLICT);
        }
        // check duplicate email
        if (userRepository.isExistedResource(null, FieldConstant.USER_EMAIL, resource.getEmail())) {
            throw new CustomException(MessageCode.MSG_EMAIL_EXISTED, HttpStatus.CONFLICT);
        }
        // check duplicate phone
        if (userRepository.isExistedResource(null, FieldConstant.USER_PHONE, resource.getPhone())) {
            throw new CustomException(MessageCode.MSG_PHONE_EXISTED, HttpStatus.CONFLICT);
        }
        // set confirm password to password
        resource.setPassword(passwordEncoder.encode(resource.getConfirmPassword()));
        // set default avatar
        if (resource.getAvatar() == null) {
            resource.setAvatar(Constant.EMP_IMAGE_PATH_DEFAULT);
        }
        // save user
        User entity = this.convertToEntity(resource.getId(), resource);
        userRepository.save(entity);
        resource.setId(entity.getId());
        // save user role
        resource.getSelectedRoles().forEach(tmp -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(entity.getId());
            userRole.setRoleId(tmp);
            userRoleRepository.save(userRole);
        });
        // set role default to user
        UserRole userRole = new UserRole();
        userRole.setUserId(entity.getId());
        userRole.setRoleId((long) Constant.ROLE_ID_DEFAULT);
        userRoleRepository.save(userRole);
        return resource;
    }

    /**
     * Update resources.
     *
     * @param id       Resource identifier
     * @param resource resource
     * @return The updated resource is returned.
     */
    @Transactional
    @Override
    protected UserResource updateResource(Long id, UserResource resource) {
        User preUser = userRepository.findByIdAndDeletedFlagFalse(id)
                                     .orElseThrow(() -> new CustomException(MessageCode.MSG_RESOURCE_NOT_EXIST,
                                                                            HttpStatus.NOT_FOUND));
        // check user default
        if (Constant.USER_ID_DEFAULT == id) {
            throw new CustomException(MessageCode.MSG_USER_NOT_UPDATED, HttpStatus.BAD_REQUEST);
        }
        // check duplicate email
        if (userRepository.isExistedResource(id, FieldConstant.USER_EMAIL, resource.getEmail())) {
            throw new CustomException(MessageCode.MSG_EMAIL_EXISTED, HttpStatus.CONFLICT);
        }
        // check duplicate phone
        if (userRepository.isExistedResource(id, FieldConstant.USER_PHONE, resource.getPhone())) {
            throw new CustomException(MessageCode.MSG_PHONE_EXISTED, HttpStatus.CONFLICT);
        }
        List<UserRole> list = userRoleRepository.findByUserIdAndDeletedFlagFalse(id);
        // get list role id of user before change
        List<Long> preRoleIds = list.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        // get list role id of user will delete
        List<Long> deleteIds = preRoleIds.stream().filter(tmp -> !resource.getSelectedRoles().contains(tmp))
                                         .collect(Collectors.toList());
        // get list role id of user will add new
        List<Long> addIds = resource.getSelectedRoles().stream().filter(tmp -> !preRoleIds.contains(tmp))
                                    .collect(Collectors.toList());
        // delete role of user
        if (!deleteIds.isEmpty()) {
            userRoleRepository.deleteByRoleId(deleteIds);
        }
        // save new role of user
        addIds.forEach(tmp -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(id);
            userRole.setRoleId(tmp);
            userRoleRepository.save(userRole);
        });
        if (resource.isLockFlag() || (preUser.getBranchId() == null && resource.getBranchId() != null) ||
            (preUser.getBranchId() != null && resource.getBranchId() == null) ||
            (preUser.getBranchId() != null && !preUser.getBranchId().equals(resource.getBranchId()))) {
            // delete all token of user
            accessTokenRepository.deleteAccessTokenByUserId(id);
        }
        User entity = convertToEntity(id, resource);
        entity.setPassword(preUser.getPassword());
        userRepository.save(entity);
        resource.setId(id);
        return resource;
    }

    /**
     * Change password.
     *
     * @param id       Resource identifier
     * @param password new password
     */
    @Transactional
    public void changePassword(Long id, String password) {
        // check user default
        if (Constant.USER_ID_DEFAULT == id) {
            throw new CustomException(MessageCode.MSG_USER_NOT_UPDATED, HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findByIdAndDeletedFlagFalse(id).orElseThrow(
                () -> new CustomException(MessageCode.MSG_USER_NOT_EXIST, HttpStatus.NOT_FOUND));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        // delete all token of old password
        accessTokenRepository.deleteAccessTokenByUserId(id);
    }

    /**
     * Change status.
     *
     * @param id Resource identifier
     */
    @Transactional
    public void changeStatus(Long id) {
        // check user default
        if (Constant.USER_ID_DEFAULT == id) {
            throw new CustomException(MessageCode.MSG_USER_NOT_UPDATED, HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findByIdAndDeletedFlagFalse(id).orElseThrow(
                () -> new CustomException(MessageCode.MSG_USER_NOT_EXIST, HttpStatus.NOT_FOUND));
        if (!user.isLockFlag()) {
            // delete all token of user
            accessTokenRepository.deleteAccessTokenByUserId(id);
        }
        user.setLockFlag(!user.isLockFlag());
        userRepository.save(user);
    }

    /**
     * Batch change status.
     *
     * @param ids list resource identifier
     */
    @Transactional
    public void batchChangeStatus(String ids, boolean status) {
        List<Long> id = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        for (Long tmp : id) {
            // check user default
            if (Constant.USER_ID_DEFAULT == tmp) {
                continue;
            }
            User user = userRepository.findByIdAndDeletedFlagFalse(tmp).orElse(null);
            if (user == null || user.isLockFlag() == status) {
                continue;
            }
            if (status) {
                // delete all token of user
                accessTokenRepository.deleteAccessTokenByUserId(tmp);
            }
            user.setLockFlag(status);
            userRepository.save(user);
        }
    }

    /**
     * Specify a resource identifier and delete the resource.
     *
     * @param id Resource identifier
     */
    @Transactional
    @Override
    public void deleteResource(Long id) {
        // check user default
        if (Constant.USER_ID_DEFAULT == id) {
            throw new CustomException(MessageCode.MSG_USER_NOT_DELETED, HttpStatus.BAD_REQUEST);
        }
        super.deleteResource(id);
        userRoleRepository.deleteByUserId(id);
    }

    /**
     * Implement the process of converting entities to resources
     *
     * @param entity entity
     * @return resource
     */
    @Override
    protected UserResource convertToResource(User entity) {
        return super.mapper.convertToResource(entity);
    }

    /**
     * Implement the process of converting resources to entities
     *
     * @param id       Resource identifier
     * @param resource resource
     * @return entity
     */
    @Override
    protected User convertToEntity(Long id, UserResource resource) {
        User user = super.mapper.convertToEntity(resource);
        user.setId(id);
        return user;
    }

    /**
     * Implement the process of converting condition string to condition class
     *
     * @param condition condition
     * @return condition
     */
    @Override
    protected UserCondition getCondition(FindCondition condition) {
        UserCondition userCondition = condition.get(UserCondition.class);
        return userCondition != null ? userCondition : new UserCondition();
    }
}
