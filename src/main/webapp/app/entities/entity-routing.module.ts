import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'continent',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jhipsterSampleApplicationContinent.home.title' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/continent/continent.module').then(m => m.JhipsterSampleApplicationContinentModule),
      },
      {
        path: 'country',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jhipsterSampleApplicationCountry.home.title' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/country/country.module').then(m => m.JhipsterSampleApplicationCountryModule),
      },
      {
        path: 'currency',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jhipsterSampleApplicationCurrency.home.title' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/currency/currency.module').then(m => m.JhipsterSampleApplicationCurrencyModule),
      },
      {
        path: 'language',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jhipsterSampleApplicationLanguage.home.title' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/language/language.module').then(m => m.JhipsterSampleApplicationLanguageModule),
      },
      {
        path: 'province',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jhipsterSampleApplicationProvince.home.title' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/province/province.module').then(m => m.JhipsterSampleApplicationProvinceModule),
      },
      {
        path: 'county',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jhipsterSampleApplicationCounty.home.title' },
        loadChildren: () => import('./jhipsterSampleApplication/county/county.module').then(m => m.JhipsterSampleApplicationCountyModule),
      },
      {
        path: 'district',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jhipsterSampleApplicationDistrict.home.title' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/district/district.module').then(m => m.JhipsterSampleApplicationDistrictModule),
      },
      {
        path: 'city',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jhipsterSampleApplicationCity.home.title' },
        loadChildren: () => import('./jhipsterSampleApplication/city/city.module').then(m => m.JhipsterSampleApplicationCityModule),
      },
      {
        path: 'rural-district',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jhipsterSampleApplicationRuralDistrict.home.title' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/rural-district/rural-district.module').then(
            m => m.JhipsterSampleApplicationRuralDistrictModule
          ),
      },
      {
        path: 'village',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jhipsterSampleApplicationVillage.home.title' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/village/village.module').then(m => m.JhipsterSampleApplicationVillageModule),
      },
      {
        path: 'asset-category',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jhipsterSampleApplicationAssetCategory.home.title' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/asset-category/asset-category.module').then(
            m => m.JhipsterSampleApplicationAssetCategoryModule
          ),
      },
      {
        path: 'asset-status',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jhipsterSampleApplicationAssetStatus.home.title' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/asset-status/asset-status.module').then(m => m.JhipsterSampleApplicationAssetStatusModule),
      },
      {
        path: 'asset',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jhipsterSampleApplicationAsset.home.title' },
        loadChildren: () => import('./jhipsterSampleApplication/asset/asset.module').then(m => m.JhipsterSampleApplicationAssetModule),
      },
      {
        path: 'asset-assign',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jhipsterSampleApplicationAssetAssign.home.title' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/asset-assign/asset-assign.module').then(m => m.JhipsterSampleApplicationAssetAssignModule),
      },
      {
        path: 'employee',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jhipsterSampleApplicationEmployee.home.title' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/employee/employee.module').then(m => m.JhipsterSampleApplicationEmployeeModule),
      },
      {
        path: 'employee-contact',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jhipsterSampleApplicationEmployeeContact.home.title' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/employee-contact/employee-contact.module').then(
            m => m.JhipsterSampleApplicationEmployeeContactModule
          ),
      },
      {
        path: 'job-opening',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jhipsterSampleApplicationJobOpening.home.title' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/job-opening/job-opening.module').then(m => m.JhipsterSampleApplicationJobOpeningModule),
      },
      {
        path: 'candidate',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jhipsterSampleApplicationCandidate.home.title' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/candidate/candidate.module').then(m => m.JhipsterSampleApplicationCandidateModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
