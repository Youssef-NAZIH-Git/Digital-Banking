package nazih.youssef.backend.dtos;

import lombok.Data;

@Data
public class DashboardStatsDTO {
    private long totalCustomers;
    private long totalAccounts;
    private long currentAccounts;
    private long savingAccounts;
    private double totalBalance;
}