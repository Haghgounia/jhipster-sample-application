import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AssetAssignDetailComponent } from './asset-assign-detail.component';

describe('AssetAssign Management Detail Component', () => {
  let comp: AssetAssignDetailComponent;
  let fixture: ComponentFixture<AssetAssignDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssetAssignDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ assetAssign: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AssetAssignDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssetAssignDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load assetAssign on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.assetAssign).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
