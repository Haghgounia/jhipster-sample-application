import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRuralDistrict } from '../rural-district.model';

@Component({
  selector: 'jhi-rural-district-detail',
  templateUrl: './rural-district-detail.component.html',
})
export class RuralDistrictDetailComponent implements OnInit {
  ruralDistrict: IRuralDistrict | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ruralDistrict }) => {
      this.ruralDistrict = ruralDistrict;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
