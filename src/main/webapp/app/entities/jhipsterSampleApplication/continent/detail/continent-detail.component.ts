import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContinent } from '../continent.model';

@Component({
  selector: 'jhi-continent-detail',
  templateUrl: './continent-detail.component.html',
})
export class ContinentDetailComponent implements OnInit {
  continent: IContinent | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ continent }) => {
      this.continent = continent;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
