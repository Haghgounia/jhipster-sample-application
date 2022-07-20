import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CountyService } from '../service/county.service';
import { ICounty, County } from '../county.model';
import { IProvince } from 'app/entities/jhipsterSampleApplication/province/province.model';
import { ProvinceService } from 'app/entities/jhipsterSampleApplication/province/service/province.service';

import { CountyUpdateComponent } from './county-update.component';

describe('County Management Update Component', () => {
  let comp: CountyUpdateComponent;
  let fixture: ComponentFixture<CountyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let countyService: CountyService;
  let provinceService: ProvinceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CountyUpdateComponent],
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
      .overrideTemplate(CountyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CountyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    countyService = TestBed.inject(CountyService);
    provinceService = TestBed.inject(ProvinceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Province query and add missing value', () => {
      const county: ICounty = { id: 456 };
      const province: IProvince = { id: 57605 };
      county.province = province;

      const provinceCollection: IProvince[] = [{ id: 12517 }];
      jest.spyOn(provinceService, 'query').mockReturnValue(of(new HttpResponse({ body: provinceCollection })));
      const additionalProvinces = [province];
      const expectedCollection: IProvince[] = [...additionalProvinces, ...provinceCollection];
      jest.spyOn(provinceService, 'addProvinceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ county });
      comp.ngOnInit();

      expect(provinceService.query).toHaveBeenCalled();
      expect(provinceService.addProvinceToCollectionIfMissing).toHaveBeenCalledWith(provinceCollection, ...additionalProvinces);
      expect(comp.provincesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const county: ICounty = { id: 456 };
      const province: IProvince = { id: 38894 };
      county.province = province;

      activatedRoute.data = of({ county });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(county));
      expect(comp.provincesSharedCollection).toContain(province);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<County>>();
      const county = { id: 123 };
      jest.spyOn(countyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ county });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: county }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(countyService.update).toHaveBeenCalledWith(county);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<County>>();
      const county = new County();
      jest.spyOn(countyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ county });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: county }));
      saveSubject.complete();

      // THEN
      expect(countyService.create).toHaveBeenCalledWith(county);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<County>>();
      const county = { id: 123 };
      jest.spyOn(countyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ county });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(countyService.update).toHaveBeenCalledWith(county);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackProvinceById', () => {
      it('Should return tracked Province primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProvinceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
