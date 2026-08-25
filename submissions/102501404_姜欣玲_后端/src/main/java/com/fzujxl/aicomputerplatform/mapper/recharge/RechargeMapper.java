package com.fzujxl.aicomputerplatform.mapper.recharge;

import com.fzujxl.aicomputerplatform.entity.RechargePackage;
import org.apache.ibatis.annotations.*;

@Mapper
public interface RechargeMapper {

    @Select("select id from recharge_package where package_name=#{name} and deleted =0 ")
    RechargePackage selectByPackageName(String name);

    @Insert("insert into recharge_package (PACKAGE_NAME, PACKAGE_TYPE, DESCRIPTION," +
            " ORIGINAL_PRICE, CURRENT_PRICE, POINTS, DAILY_POINTS, DURATION_DAYS, STATUS, SORT_ORDER, " +
            "CREATED_TIME, UPDATED_TIME, DELETED) values (#{rechargePackage.packageName}, " +
            "#{rechargePackage.packageType}, #{rechargePackage.packageDescription}, " +
            " #{rechargePackage.originalPrice}, #{rechargePackage.currentPrice}," +
            " #{rechargePackage.points}, #{rechargePackage.dailyPoints}, #{rechargePackage.duration}, " +
            "#{rechargePackage.status}, #{sortOrder}, #{rechargePackage.createdTime}," +
            " #{rechargePackage.updatedTime}, #{rechargePackage.deleted})")
    int insert(RechargePackage rechargePackage);

    @Update("update recharge_package set status=#{status} where package_name=#{packageName}" +
            " and status!= #{status} and deleted=0")
    int updateByPackageName(@Param("packageName") String packageName, @Param("status") Integer status);
}
