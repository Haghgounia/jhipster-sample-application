import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmployeeContact } from '../employee-contact.model';

@Component({
  selector: 'jhi-employee-contact-detail',
  templateUrl: './employee-contact-detail.component.html',
})
export class EmployeeContactDetailComponent implements OnInit {
  employeeContact: IEmployeeContact | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeContact }) => {
      this.employeeContact = employeeContact;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
