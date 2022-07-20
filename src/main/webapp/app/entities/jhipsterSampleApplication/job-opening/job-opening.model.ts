export interface IJobOpening {
  id?: number;
  postingTitle?: string | null;
  jobStatus?: number | null;
  hiringLead?: number | null;
  departmentId?: number | null;
  employmentType?: number | null;
  minimumExperience?: number | null;
  jobDescription?: string | null;
}

export class JobOpening implements IJobOpening {
  constructor(
    public id?: number,
    public postingTitle?: string | null,
    public jobStatus?: number | null,
    public hiringLead?: number | null,
    public departmentId?: number | null,
    public employmentType?: number | null,
    public minimumExperience?: number | null,
    public jobDescription?: string | null
  ) {}
}

export function getJobOpeningIdentifier(jobOpening: IJobOpening): number | undefined {
  return jobOpening.id;
}
