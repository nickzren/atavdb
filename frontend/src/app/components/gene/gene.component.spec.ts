import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { GeneComponent } from './gene.component';

describe('GeneComponent', () => {
  let component: GeneComponent;
  let fixture: ComponentFixture<GeneComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ GeneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GeneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
