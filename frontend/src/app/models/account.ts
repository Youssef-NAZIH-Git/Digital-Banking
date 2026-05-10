export interface AccountDetails {
    accountId:            string;
    accountOperationDTOS: AccountOperation[];
    balance:              number;
    currentPage:          number;
    pageSize:             number;
    totalPages:           number;
}

export interface AccountOperation {
    amount:        number;
    description:   string;
    id:            number;
    operationDate: Date;
    type:          string;
}