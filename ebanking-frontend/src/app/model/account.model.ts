export interface AccountDetails {
  accountId:            string;
  balance:              number;
  type:                 string;
  currentPage:          number;
  totalPages:           number;
  pageSize:             number;
  accountOperationDTOS: AccountOperation[];
}

export interface AccountOperation {
  id:            number;
  operationDate: Date;
  amount:        number;
  type:          string;
  description:   string;
}

export interface BankAccount {
  id:           string;
  type:         string;
  balance:      number;
  status:       string;
  createdAt:    Date;
  customerDTO:  { id: number; name: string; email: string };
  overdraft?:   number;
  interestRate?: number;
}