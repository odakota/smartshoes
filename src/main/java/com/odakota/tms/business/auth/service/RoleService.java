package com.odakota.tms.business.auth.service;

import com.odakota.tms.business.auth.entity.Role;
import com.odakota.tms.business.auth.repository.PermissionRoleRepository;
import com.odakota.tms.business.auth.repository.RoleRepository;
import com.odakota.tms.business.auth.repository.UserRoleRepository;
import com.odakota.tms.business.auth.resource.RoleResource;
import com.odakota.tms.business.auth.resource.RoleResource.RoleCondition;
import com.odakota.tms.constant.Constant;
import com.odakota.tms.constant.FieldConstant;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.system.base.BaseParameter.FindCondition;
import com.odakota.tms.system.base.BaseService;
import com.odakota.tms.system.config.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class RoleService extends BaseService<Role, RoleResource, RoleCondition> {

    private final RoleRepository roleRepository;

    private final PermissionRoleRepository permissionRoleRepository;

    private final UserRoleRepository userRoleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository,
                       PermissionRoleRepository permissionRoleRepository,
                       UserRoleRepository userRoleRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository;
        this.permissionRoleRepository = permissionRoleRepository;
        this.userRoleRepository = userRoleRepository;
    }


    /**
     * Create a new role.
     *
     * @param resource resource
     * @return The created role is returned.
     */
    @Override
    public RoleResource createResource(RoleResource resource) {
        // check duplicate roleCode
        if (roleRepository.isExistedResource(null, FieldConstant.ROLE_CODE, resource.getRoleCode())) {
            throw new CustomException(MessageCode.MSG_ROLE_CODE_EXISTED, HttpStatus.CONFLICT);
        }
        return super.createResource(resource);
    }

    /**
     * Update resources.
     *
     * @param id       Resource identifier
     * @param resource resource
     * @return The updated resource is returned.
     */
    @Override
    protected RoleResource updateResource(Long id, RoleResource resource) {
        // check role default
        if (Constant.NUMBER_OF_ROLE_DEFAULT >= id) {
            throw new CustomException(MessageCode.MSG_ROLE_NOT_UPDATED, HttpStatus.BAD_REQUEST);
        }
        return super.updateResource(id, resource);
    }

    /**
     * Delete role by id.
     *
     * @param id Resource identifier
     */
    @Override
    @Transactional
    public void deleteResource(Long id) {
        // check role default
        if (Constant.NUMBER_OF_ROLE_DEFAULT >= id) {
            throw new CustomException(MessageCode.MSG_ROLE_NOT_DELETED, HttpStatus.BAD_REQUEST);
        }
        permissionRoleRepository.deleteByRoleId(id);
        userRoleRepository.deleteByRoleId(id);
        super.deleteResource(id);
    }

    /**
     * Implement the process of converting entities to resources
     *
     * @param entity entity
     * @return resource
     */
    @Override
    protected RoleResource convertToResource(Role entity) {
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
    protected Role convertToEntity(Long id, RoleResource resource) {
        Role entity = super.mapper.convertToEntity(resource);
        entity.setId(id);
        return entity;
    }

    /**
     * Implement the process of converting condition string to condition class
     *
     * @param condition condition
     * @return condition
     */
    @Override
    protected RoleCondition getCondition(FindCondition condition) {
        RoleCondition roleCondition = condition.get(RoleCondition.class);
        return roleCondition != null ? roleCondition : new RoleCondition();
    }
}
