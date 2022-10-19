import { TestBed } from '@angular/core/testing';

import { ParseService } from './parse.service';
import {HttpClient} from "@angular/common/http";
import {FormComponent} from "./form/form.component";

describe('ParseService', () => {
  let service: ParseService;

  beforeEach(() => {
    const mockHttpClient = jasmine.createSpyObj('HttpClient', ['post'])
    TestBed.configureTestingModule({
      providers: [{provide: HttpClient, useValue: mockHttpClient}]
    });
    service = TestBed.inject(ParseService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
