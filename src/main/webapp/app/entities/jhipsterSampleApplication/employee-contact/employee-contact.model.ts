export interface IEmployeeContact {
  id?: number;
  employeeContactId?: number | null;
  employeeId?: number | null;
  contactType?: number | null;
  contact?: string | null;
}

export class EmployeeContact implements IEmployeeContact {
  constructor(
    public id?: number,
    public employeeContactId?: number | null,
    public employeeId?: number | null,
    public contactType?: number | null,
    public contact?: string | null
  ) {}
}

export function getEmployeeContactIdentifier(employeeContact: IEmployeeContact): number | undefined {
  return employeeContact.id;
}
