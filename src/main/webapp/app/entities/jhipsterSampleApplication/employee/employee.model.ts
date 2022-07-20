export interface IEmployee {
  id?: number;
  employeeId?: number | null;
  firstName?: string | null;
  lastName?: string | null;
  gender?: number | null;
  maritalStatus?: number | null;
  birthDate?: number | null;
  hireDate?: number | null;
  employmentStatus?: number | null;
  managerId?: number | null;
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public employeeId?: number | null,
    public firstName?: string | null,
    public lastName?: string | null,
    public gender?: number | null,
    public maritalStatus?: number | null,
    public birthDate?: number | null,
    public hireDate?: number | null,
    public employmentStatus?: number | null,
    public managerId?: number | null
  ) {}
}

export function getEmployeeIdentifier(employee: IEmployee): number | undefined {
  return employee.id;
}
