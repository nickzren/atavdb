import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IgvComponent } from './igv.component';

describe('IgvComponent', () => {
  let component: IgvComponent;
  let fixture: ComponentFixture<IgvComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IgvComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IgvComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
