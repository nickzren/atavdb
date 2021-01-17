import { TestBed } from '@angular/core/testing';

import { SampleCountService } from './sample-count.service';

describe('SampleCountService', () => {
  let service: SampleCountService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SampleCountService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
