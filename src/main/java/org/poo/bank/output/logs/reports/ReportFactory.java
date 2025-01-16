package org.poo.bank.output.logs.reports;

public class ReportFactory {
    public static Report createReport(Report.ReportType reportType, String iban) {
        switch (reportType) {
            case ACCOUNT -> {
                return new AccountReport(iban);
            }
            case SPENDING -> {
                return new SpendingReport(iban);
            }
            case BUSINESS -> {
                return new BusinessReport(iban);
            }
            default -> {
                throw new IllegalArgumentException("Unsupported report type: " + reportType);
            }
        }
    }
}
