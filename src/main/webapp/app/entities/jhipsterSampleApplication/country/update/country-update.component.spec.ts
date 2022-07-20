import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CountryService } from '../service/country.service';
import { ICountry, Country } from '../country.model';
import { IContinent } from 'app/entities/jhipsterSampleApplication/continent/continent.model';
import { ContinentService } from 'app/entities/jhipsterSampleApplication/continent/service/continent.service';
import { ICurrency } from 'app/entities/jhipsterSampleApplication/currency/currency.model';
import { CurrencyService } from 'app/entities/jhipsterSampleApplication/currency/service/currency.service';
import { ILanguage } from 'app/entities/jhipsterSampleApplication/language/language.model';
import { LanguageService } from 'app/entities/jhipsterSampleApplication/language/service/language.service';

import { CountryUpdateComponent } from './country-update.component';

describe('Country Management Update Component', () => {
  let comp: CountryUpdateComponent;
  let fixture: ComponentFixture<CountryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let countryService: CountryService;
  let continentService: ContinentService;
  let currencyService: CurrencyService;
  let languageService: LanguageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CountryUpdateComponent],
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
      .overrideTemplate(CountryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CountryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    countryService = TestBed.inject(CountryService);
    continentService = TestBed.inject(ContinentService);
    currencyService = TestBed.inject(CurrencyService);
    languageService = TestBed.inject(LanguageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Continent query and add missing value', () => {
      const country: ICountry = { id: 456 };
      const continent: IContinent = { id: 59775 };
      country.continent = continent;

      const continentCollection: IContinent[] = [{ id: 38560 }];
      jest.spyOn(continentService, 'query').mockReturnValue(of(new HttpResponse({ body: continentCollection })));
      const additionalContinents = [continent];
      const expectedCollection: IContinent[] = [...additionalContinents, ...continentCollection];
      jest.spyOn(continentService, 'addContinentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ country });
      comp.ngOnInit();

      expect(continentService.query).toHaveBeenCalled();
      expect(continentService.addContinentToCollectionIfMissing).toHaveBeenCalledWith(continentCollection, ...additionalContinents);
      expect(comp.continentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Currency query and add missing value', () => {
      const country: ICountry = { id: 456 };
      const currency: ICurrency = { id: 45585 };
      country.currency = currency;

      const currencyCollection: ICurrency[] = [{ id: 32443 }];
      jest.spyOn(currencyService, 'query').mockReturnValue(of(new HttpResponse({ body: currencyCollection })));
      const additionalCurrencies = [currency];
      const expectedCollection: ICurrency[] = [...additionalCurrencies, ...currencyCollection];
      jest.spyOn(currencyService, 'addCurrencyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ country });
      comp.ngOnInit();

      expect(currencyService.query).toHaveBeenCalled();
      expect(currencyService.addCurrencyToCollectionIfMissing).toHaveBeenCalledWith(currencyCollection, ...additionalCurrencies);
      expect(comp.currenciesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Language query and add missing value', () => {
      const country: ICountry = { id: 456 };
      const officialLanguage: ILanguage = { id: 40198 };
      country.officialLanguage = officialLanguage;

      const languageCollection: ILanguage[] = [{ id: 76721 }];
      jest.spyOn(languageService, 'query').mockReturnValue(of(new HttpResponse({ body: languageCollection })));
      const additionalLanguages = [officialLanguage];
      const expectedCollection: ILanguage[] = [...additionalLanguages, ...languageCollection];
      jest.spyOn(languageService, 'addLanguageToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ country });
      comp.ngOnInit();

      expect(languageService.query).toHaveBeenCalled();
      expect(languageService.addLanguageToCollectionIfMissing).toHaveBeenCalledWith(languageCollection, ...additionalLanguages);
      expect(comp.languagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const country: ICountry = { id: 456 };
      const continent: IContinent = { id: 11690 };
      country.continent = continent;
      const currency: ICurrency = { id: 39960 };
      country.currency = currency;
      const officialLanguage: ILanguage = { id: 23109 };
      country.officialLanguage = officialLanguage;

      activatedRoute.data = of({ country });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(country));
      expect(comp.continentsSharedCollection).toContain(continent);
      expect(comp.currenciesSharedCollection).toContain(currency);
      expect(comp.languagesSharedCollection).toContain(officialLanguage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Country>>();
      const country = { id: 123 };
      jest.spyOn(countryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ country });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: country }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(countryService.update).toHaveBeenCalledWith(country);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Country>>();
      const country = new Country();
      jest.spyOn(countryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ country });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: country }));
      saveSubject.complete();

      // THEN
      expect(countryService.create).toHaveBeenCalledWith(country);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Country>>();
      const country = { id: 123 };
      jest.spyOn(countryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ country });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(countryService.update).toHaveBeenCalledWith(country);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackContinentById', () => {
      it('Should return tracked Continent primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackContinentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCurrencyById', () => {
      it('Should return tracked Currency primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCurrencyById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackLanguageById', () => {
      it('Should return tracked Language primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLanguageById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
