import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BookingComponent } from'./booking/booking.component';
import { HomepageComponent } from './homepage/homepage.component';
import { PaymentComponent } from './payment/payment.component';
import { StatusComponent } from './status/status.component';
import { FeedbackComponent } from './feedback/feedback.component';


const routes: Routes = [
  { path: 'homepage', component: HomepageComponent },
  { path: 'booking', component: BookingComponent },
  { path: 'feedback', component: FeedbackComponent },
  { path: 'payment', component: PaymentComponent},
  { path: 'status', component: StatusComponent },
  { path: '', redirectTo: '/homepage', pathMatch: 'full' },
  { path: '**', redirectTo: 'homepage', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
