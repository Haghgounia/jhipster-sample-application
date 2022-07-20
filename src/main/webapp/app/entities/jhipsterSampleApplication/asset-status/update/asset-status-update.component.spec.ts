import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AssetStatusService } from '../service/asset-status.service';
import { IAssetStatus, AssetStatus } from '../asset-status.model';

import { AssetStatusUpdateComponent } from './asset-status-update.component';

describe('AssetStatus Management Update Component', () => {
  let comp: AssetStatusUpdateComponent;
  let fixture: ComponentFixture<AssetStatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetStatusService: AssetStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AssetStatusUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AssetStatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetStatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetStatusService = TestBed.inject(AssetStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const assetStatus: IAssetStatus = { id: 456 };

      activatedRoute.data = of({ assetStatus });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(assetStatus));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetStatus>>();
      const assetStatus = { id: 123 };
      jest.spyOn(assetStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetStatus }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetStatusService.update).toHaveBeenCalledWith(assetStatus);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetStatus>>();
      const assetStatus = new AssetStatus();
      jest.spyOn(assetStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetStatus }));
      saveSubject.complete();

      // THEN
      expect(assetStatusService.create).toHaveBeenCalledWith(assetStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetStatus>>();
      const assetStatus = { id: 123 };
      jest.spyOn(assetStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetStatusService.update).toHaveBeenCalledWith(assetStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
