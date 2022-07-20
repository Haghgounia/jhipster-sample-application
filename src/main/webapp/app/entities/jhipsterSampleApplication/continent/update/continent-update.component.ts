import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IContinent, Continent } from '../continent.model';
import { ContinentService } from '../service/continent.service';

@Component({
  selector: 'jhi-continent-update',
  templateUrl: './continent-update.component.html',
})
export class ContinentUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    continentId: [null, [Validators.required]],
    continentCode: [null, [Validators.required]],
    continentName: [null, [Validators.required]],
    continentEnglishName: [],
  });

  constructor(protected continentService: ContinentService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ continent }) => {
      this.updateForm(continent);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const continent = this.createFromForm();
    if (continent.id !== undefined) {
      this.subscribeToSaveResponse(this.continentService.update(continent));
    } else {
      this.subscribeToSaveResponse(this.continentService.create(continent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContinent>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(continent: IContinent): void {
    this.editForm.patchValue({
      id: continent.id,
      continentId: continent.continentId,
      continentCode: continent.continentCode,
      continentName: continent.continentName,
      continentEnglishName: continent.continentEnglishName,
    });
  }

  protected createFromForm(): IContinent {
    return {
      ...new Continent(),
      id: this.editForm.get(['id'])!.value,
      continentId: this.editForm.get(['continentId'])!.value,
      continentCode: this.editForm.get(['continentCode'])!.value,
      continentName: this.editForm.get(['continentName'])!.value,
      continentEnglishName: this.editForm.get(['continentEnglishName'])!.value,
    };
  }
}
