package com.fzujxl.aicomputerplatform.service.recharge;

import com.fzujxl.aicomputerplatform.dto.rechage.*;

public interface RechargeService {
     AddRechargePackageResponse addRechargePackage(AddRechargePackageRequest request);
     ChangeRechargePackageStatusResponse publishRechargePackage(PublishRechargePackageRequest request);
     ChangeRechargePackageStatusResponse offlineRechargePackage(OfflineRechargePackageRequest request);
}