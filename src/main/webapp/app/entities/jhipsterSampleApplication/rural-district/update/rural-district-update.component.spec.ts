import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RuralDistrictService } from '../service/rural-district.service';
import { IRuralDistrict, RuralDistrict } from '../rural-district.model';
import { IDistrict } from 'app/entities/jhipsterSampleApplication/district/district.model';
import { DistrictService } from 'app/entities/jhipsterSampleApplication/district/service/district.service';

import { RuralDistrictUpdateComponent } from './rural-district-update.component';

describe('RuralDistrict Management Update Component', () => {
  let comp: RuralDistrictUpdateComponent;
  let fixture: ComponentFixture<RuralDistrictUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ruralDistrictService: RuralDistrictService;
  let districtService: DistrictService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RuralDistrictUpdateComponent],
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
      .overrideTemplate(RuralDistrictUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RuralDistrictUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ruralDistrictService = TestBed.inject(RuralDistrictService);
    districtService = TestBed.inject(DistrictService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call District query and add missing value', () => {
      const ruralDistrict: IRuralDistrict = { id: 456 };
      const district: IDistrict = { id: 99760 };
      ruralDistrict.district = district;

      const districtCollection: IDistrict[] = [{ id: 76538 }];
      jest.spyOn(districtService, 'query').mockReturnValue(of(new HttpResponse({ body: districtCollection })));
      const additionalDistricts = [district];
      const expectedCollection: IDistrict[] = [...additionalDistricts, ...districtCollection];
      jest.spyOn(districtService, 'addDistrictToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ruralDistrict });
      comp.ngOnInit();

      expect(districtService.query).toHaveBeenCalled();
      expect(districtService.addDistrictToCollectionIfMissing).toHaveBeenCalledWith(districtCollection, ...additionalDistricts);
      expect(comp.districtsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ruralDistrict: IRuralDistrict = { id: 456 };
      const district: IDistrict = { id: 89946 };
      ruralDistrict.district = district;

      activatedRoute.data = of({ ruralDistrict });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(ruralDistrict));
      expect(comp.districtsSharedCollection).toContain(district);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RuralDistrict>>();
      const ruralDistrict = { id: 123 };
      jest.spyOn(ruralDistrictService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ruralDistrict });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ruralDistrict }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(ruralDistrictService.update).toHaveBeenCalledWith(ruralDistrict);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RuralDistrict>>();
      const ruralDistrict = new RuralDistrict();
      jest.spyOn(ruralDistrictService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ruralDistrict });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ruralDistrict }));
      saveSubject.complete();

      // THEN
      expect(ruralDistrictService.create).toHaveBeenCalledWith(ruralDistrict);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RuralDistrict>>();
      const ruralDistrict = { id: 123 };
      jest.spyOn(ruralDistrictService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ruralDistrict });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ruralDistrictService.update).toHaveBeenCalledWith(ruralDistrict);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDistrictById', () => {
      it('Should return tracked District primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDistrictById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
