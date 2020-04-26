package com.odakota.tms.business.sales.repository;

import com.odakota.tms.business.sales.entity.SalesOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface SalesOrderDetailRepository extends JpaRepository<SalesOrderDetail, Long> {

    @Query(value = "select od.* from sales_order_detail_tbl od join sales_order_tbl sot on od.sales_order_id = sot.id " +
                   "where sot.branch_id = ?1 and to_char(sot.created_date, 'YYYY-MM-DD') = ?2 and sot.status = 2",
           nativeQuery = true)
    List<SalesOrderDetail> findAllYesterdayByBranch(Long branchId, String date);

    @Query(value = "select od.* from sales_order_detail_tbl od join sales_order_tbl sot on od.sales_order_id = sot.id " +
                   "where ( :branchId is null or sot.branch_id = :branchId) and to_char(sot.created_date, :format)  = :month " +
                   "and sot.status = 2", nativeQuery = true)
    List<SalesOrderDetail> findAllPreMonthByBranch(@Param("branchId") Long branchId, @Param("month") String month,
                                                   @Param("format") String format);

    @Query("select new com.odakota.tms.business.sales.entity.SalesOrderDetail(p.code, p.name, s.prePrice, s.lstPrice, " +
           "s.amountProduct) from SalesOrderDetail s join Product p " +
           "on s.productId = p.id where s.saleOrderId = ?1")
    List<SalesOrderDetail> findByOrderId(Long orderId);
}
