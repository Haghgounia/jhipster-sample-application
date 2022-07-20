import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJobOpening } from '../job-opening.model';

@Component({
  selector: 'jhi-job-opening-detail',
  templateUrl: './job-opening-detail.component.html',
})
export class JobOpeningDetailComponent implements OnInit {
  jobOpening: IJobOpening | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobOpening }) => {
      this.jobOpening = jobOpening;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
