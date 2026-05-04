import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../services/customer.service';

@Component({
  selector: 'app-new-customer',
  templateUrl: './new-customer.component.html',
  styleUrls: ['./new-customer.component.css']
})
export class NewCustomerComponent implements OnInit {
  newCustomerFormGroup!: FormGroup;
  editMode = false;
  editId: number | null = null;
  saving = false;

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.newCustomerFormGroup = this.fb.group({
      name:  this.fb.control(null, [Validators.required, Validators.minLength(4)]),
      email: this.fb.control(null, [Validators.required, Validators.email])
    });
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.editMode = true;
      this.editId = +id;
      this.customerService.getCustomer(this.editId).subscribe({
        next: c => this.newCustomerFormGroup.patchValue({ name: c.name, email: c.email }),
        error: err => console.error(err)
      });
    }
  }

  handleSaveCustomer(): void {
    if (this.newCustomerFormGroup.invalid) return;
    this.saving = true;
    const customer = this.newCustomerFormGroup.value;
    const obs = this.editMode && this.editId
      ? this.customerService.updateCustomer(this.editId, customer)
      : this.customerService.saveCustomer(customer);
    obs.subscribe({
      next: () => {
        this.saving = false;
        this.router.navigateByUrl('/admin/customers');
      },
      error: err => { console.error(err); this.saving = false; }
    });
  }
}