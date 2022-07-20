import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AssetAssignService } from '../service/asset-assign.service';
import { IAssetAssign, AssetAssign } from '../asset-assign.model';

import { AssetAssignUpdateComponent } from './asset-assign-update.component';

describe('AssetAssign Management Update Component', () => {
  let comp: AssetAssignUpdateComponent;
  let fixture: ComponentFixture<AssetAssignUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetAssignService: AssetAssignService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AssetAssignUpdateComponent],
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
      .overrideTemplate(AssetAssignUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetAssignUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetAssignService = TestBed.inject(AssetAssignService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const assetAssign: IAssetAssign = { id: 456 };

      activatedRoute.data = of({ assetAssign });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(assetAssign));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetAssign>>();
      const assetAssign = { id: 123 };
      jest.spyOn(assetAssignService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetAssign });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetAssign }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetAssignService.update).toHaveBeenCalledWith(assetAssign);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetAssign>>();
      const assetAssign = new AssetAssign();
      jest.spyOn(assetAssignService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetAssign });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetAssign }));
      saveSubject.complete();

      // THEN
      expect(assetAssignService.create).toHaveBeenCalledWith(assetAssign);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetAssign>>();
      const assetAssign = { id: 123 };
      jest.spyOn(assetAssignService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetAssign });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetAssignService.update).toHaveBeenCalledWith(assetAssign);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
