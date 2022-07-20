import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AssetCategoryService } from '../service/asset-category.service';
import { IAssetCategory, AssetCategory } from '../asset-category.model';

import { AssetCategoryUpdateComponent } from './asset-category-update.component';

describe('AssetCategory Management Update Component', () => {
  let comp: AssetCategoryUpdateComponent;
  let fixture: ComponentFixture<AssetCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetCategoryService: AssetCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AssetCategoryUpdateComponent],
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
      .overrideTemplate(AssetCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetCategoryService = TestBed.inject(AssetCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const assetCategory: IAssetCategory = { id: 456 };

      activatedRoute.data = of({ assetCategory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(assetCategory));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetCategory>>();
      const assetCategory = { id: 123 };
      jest.spyOn(assetCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetCategory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetCategoryService.update).toHaveBeenCalledWith(assetCategory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetCategory>>();
      const assetCategory = new AssetCategory();
      jest.spyOn(assetCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetCategory }));
      saveSubject.complete();

      // THEN
      expect(assetCategoryService.create).toHaveBeenCalledWith(assetCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetCategory>>();
      const assetCategory = { id: 123 };
      jest.spyOn(assetCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetCategoryService.update).toHaveBeenCalledWith(assetCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
