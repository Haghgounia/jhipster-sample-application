import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DistrictService } from '../service/district.service';
import { IDistrict, District } from '../district.model';
import { ICounty } from 'app/entities/jhipsterSampleApplication/county/county.model';
import { CountyService } from 'app/entities/jhipsterSampleApplication/county/service/county.service';

import { DistrictUpdateComponent } from './district-update.component';

describe('District Management Update Component', () => {
  let comp: DistrictUpdateComponent;
  let fixture: ComponentFixture<DistrictUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let districtService: DistrictService;
  let countyService: CountyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DistrictUpdateComponent],
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
      .overrideTemplate(DistrictUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DistrictUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    districtService = TestBed.inject(DistrictService);
    countyService = TestBed.inject(CountyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call County query and add missing value', () => {
      const district: IDistrict = { id: 456 };
      const county: ICounty = { id: 90987 };
      district.county = county;

      const countyCollection: ICounty[] = [{ id: 71019 }];
      jest.spyOn(countyService, 'query').mockReturnValue(of(new HttpResponse({ body: countyCollection })));
      const additionalCounties = [county];
      const expectedCollection: ICounty[] = [...additionalCounties, ...countyCollection];
      jest.spyOn(countyService, 'addCountyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ district });
      comp.ngOnInit();

      expect(countyService.query).toHaveBeenCalled();
      expect(countyService.addCountyToCollectionIfMissing).toHaveBeenCalledWith(countyCollection, ...additionalCounties);
      expect(comp.countiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const district: IDistrict = { id: 456 };
      const county: ICounty = { id: 99517 };
      district.county = county;

      activatedRoute.data = of({ district });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(district));
      expect(comp.countiesSharedCollection).toContain(county);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<District>>();
      const district = { id: 123 };
      jest.spyOn(districtService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ district });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: district }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(districtService.update).toHaveBeenCalledWith(district);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<District>>();
      const district = new District();
      jest.spyOn(districtService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ district });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: district }));
      saveSubject.complete();

      // THEN
      expect(districtService.create).toHaveBeenCalledWith(district);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<District>>();
      const district = { id: 123 };
      jest.spyOn(districtService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ district });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(districtService.update).toHaveBeenCalledWith(district);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCountyById', () => {
      it('Should return tracked County primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCountyById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
