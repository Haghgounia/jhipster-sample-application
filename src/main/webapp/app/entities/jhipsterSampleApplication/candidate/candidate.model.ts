export interface ICandidate {
  id?: number;
  jobOpening?: number | null;
  candidateStatus?: number | null;
  candidateRating?: number | null;
}

export class Candidate implements ICandidate {
  constructor(
    public id?: number,
    public jobOpening?: number | null,
    public candidateStatus?: number | null,
    public candidateRating?: number | null
  ) {}
}

export function getCandidateIdentifier(candidate: ICandidate): number | undefined {
  return candidate.id;
}
