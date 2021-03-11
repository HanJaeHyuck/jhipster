/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import * as config from '@/shared/config/config';
import TicketUpdateComponent from '@/entities/ticket/ticket-update.vue';
import TicketClass from '@/entities/ticket/ticket-update.component';
import TicketService from '@/entities/ticket/ticket.service';

import AttachmentService from '@/entities/attachment/attachment.service';

import ProjectService from '@/entities/project/project.service';

import UserService from '@/admin/user-management/user-management.service';

import LabelService from '@/entities/label/label.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Ticket Management Update Component', () => {
    let wrapper: Wrapper<TicketClass>;
    let comp: TicketClass;
    let ticketServiceStub: SinonStubbedInstance<TicketService>;

    beforeEach(() => {
      ticketServiceStub = sinon.createStubInstance<TicketService>(TicketService);

      wrapper = shallowMount<TicketClass>(TicketUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          ticketService: () => ticketServiceStub,

          attachmentService: () => new AttachmentService(),

          projectService: () => new ProjectService(),

          userService: () => new UserService(),

          labelService: () => new LabelService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('load', () => {
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.ticket = entity;
        ticketServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(ticketServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.ticket = entity;
        ticketServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(ticketServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTicket = { id: 123 };
        ticketServiceStub.find.resolves(foundTicket);
        ticketServiceStub.retrieve.resolves([foundTicket]);

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
