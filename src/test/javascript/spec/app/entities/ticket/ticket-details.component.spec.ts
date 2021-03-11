/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TicketDetailComponent from '@/entities/ticket/ticket-details.vue';
import TicketClass from '@/entities/ticket/ticket-details.component';
import TicketService from '@/entities/ticket/ticket.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Ticket Management Detail Component', () => {
    let wrapper: Wrapper<TicketClass>;
    let comp: TicketClass;
    let ticketServiceStub: SinonStubbedInstance<TicketService>;

    beforeEach(() => {
      ticketServiceStub = sinon.createStubInstance<TicketService>(TicketService);

      wrapper = shallowMount<TicketClass>(TicketDetailComponent, {
        store,
        localVue,
        router,
        provide: { ticketService: () => ticketServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTicket = { id: 123 };
        ticketServiceStub.find.resolves(foundTicket);

        // WHEN
        comp.retrieveTicket(123);
        await comp.$nextTick();

        // THEN
        expect(comp.ticket).toBe(foundTicket);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTicket = { id: 123 };
        ticketServiceStub.find.resolves(foundTicket);

        // WHEN
        comp.beforeRouteEnter({ params: { ticketId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.ticket).toBe(foundTicket);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
