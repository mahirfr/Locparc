import { TestBed } from '@angular/core/testing';

import { LenderGuard } from './lender.guard';

describe('LenderGuard', () => {
  let guard: LenderGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(LenderGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
