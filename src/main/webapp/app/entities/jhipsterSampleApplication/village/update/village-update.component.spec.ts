import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VillageService } from '../service/village.service';
import { IVillage, Village } from '../village.model';
import { IRuralDistrict } from 'app/entities/jhipsterSampleApplication/rural-district/rural-district.model';
import { RuralDistrictService } from 'app/entities/jhipsterSampleApplication/rural-district/service/rural-district.service';

import { VillageUpdateComponent } from './village-update.component';

describe('Village Management Update Component', () => {
  let comp: VillageUpdateComponent;
  let fixture: ComponentFixture<VillageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let villageService: VillageService;
  let ruralDistrictService: RuralDistrictService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VillageUpdateComponent],
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
      .overrideTemplate(VillageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VillageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    villageService = TestBed.inject(VillageService);
    ruralDistrictService = TestBed.inject(RuralDistrictService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call RuralDistrict query and add missing value', () => {
      const village: IVillage = { id: 456 };
      const ruralDistrict: IRuralDistrict = { id: 70957 };
      village.ruralDistrict = ruralDistrict;

      const ruralDistrictCollection: IRuralDistrict[] = [{ id: 24749 }];
      jest.spyOn(ruralDistrictService, 'query').mockReturnValue(of(new HttpResponse({ body: ruralDistrictCollection })));
      const additionalRuralDistricts = [ruralDistrict];
      const expectedCollection: IRuralDistrict[] = [...additionalRuralDistricts, ...ruralDistrictCollection];
      jest.spyOn(ruralDistrictService, 'addRuralDistrictToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ village });
      comp.ngOnInit();

      expect(ruralDistrictService.query).toHaveBeenCalled();
      expect(ruralDistrictService.addRuralDistrictToCollectionIfMissing).toHaveBeenCalledWith(
        ruralDistrictCollection,
        ...additionalRuralDistricts
      );
      expect(comp.ruralDistrictsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const village: IVillage = { id: 456 };
      const ruralDistrict: IRuralDistrict = { id: 42185 };
      village.ruralDistrict = ruralDistrict;

      activatedRoute.data = of({ village });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(village));
      expect(comp.ruralDistrictsSharedCollection).toContain(ruralDistrict);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Village>>();
      const village = { id: 123 };
      jest.spyOn(villageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ village });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: village }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(villageService.update).toHaveBeenCalledWith(village);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Village>>();
      const village = new Village();
      jest.spyOn(villageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ village });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: village }));
      saveSubject.complete();

      // THEN
      expect(villageService.create).toHaveBeenCalledWith(village);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Village>>();
      const village = { id: 123 };
      jest.spyOn(villageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ village });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(villageService.update).toHaveBeenCalledWith(village);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackRuralDistrictById', () => {
      it('Should return tracked RuralDistrict primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRuralDistrictById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
