import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AccountService } from '../../services/account.service';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})

export class SigninComponent implements OnInit {

  form: FormGroup;
  loading = false;
  submitted = false;
  returnUrl: string;
  errorMessage: string;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AccountService) { 
      // redirect to home if already signed in
      if (this.authenticationService.isAuthenticated()) { 
        this.router.navigate(['/']);
    }
    }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  // convenience getter for easy access to form fields
  get f() { return this.form.controls; }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.form.invalid) {
      return;
    }

    this.authenticationService
      .authenticate(this.f.username.value, this.f.password.value)
      .subscribe(
        isValid => {
          if (isValid) {
            sessionStorage.setItem('authenticatedUser', this.f.username.value);
            this.authenticationService.authorize(this.f.username.value);
            this.router.navigate([this.returnUrl]);
          } else {
            this.errorMessage = "Invalid CUMC MC account username/password.";
          }
        });
  }
}
