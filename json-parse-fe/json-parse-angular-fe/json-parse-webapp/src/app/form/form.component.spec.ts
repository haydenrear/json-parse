import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { FormComponent } from './form.component';
import {ParseService} from "../parse.service";
import {of} from "rxjs";
import {HttpClient} from "@angular/common/http";

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  const mockHttpClient = jasmine.createSpyObj('HttpClient', ['post'])
  let mockParseService = new ParseService(mockHttpClient);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FormComponent],
      providers: [{provide: ParseService, useValue: mockParseService}, {provide: HttpClient, useValue: mockHttpClient}]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should update the page with hello world',  fakeAsync(() => {
    spyOn(mockParseService, 'sendRequest').and.returnValue(of('hello world ...'));
    component.sendParse();
    tick();
    expect(component.response).toBe('hello world ...');
  }));


});
