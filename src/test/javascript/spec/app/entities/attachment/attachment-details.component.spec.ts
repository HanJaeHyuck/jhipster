/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import AttachmentDetailComponent from '@/entities/attachment/attachment-details.vue';
import AttachmentClass from '@/entities/attachment/attachment-details.component';
import AttachmentService from '@/entities/attachment/attachment.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Attachment Management Detail Component', () => {
    let wrapper: Wrapper<AttachmentClass>;
    let comp: AttachmentClass;
    let attachmentServiceStub: SinonStubbedInstance<AttachmentService>;

    beforeEach(() => {
      attachmentServiceStub = sinon.createStubInstance<AttachmentService>(AttachmentService);

      wrapper = shallowMount<AttachmentClass>(AttachmentDetailComponent, {
        store,
        localVue,
        router,
        provide: { attachmentService: () => attachmentServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundAttachment = { id: 123 };
        attachmentServiceStub.find.resolves(foundAttachment);

        // WHEN
        comp.retrieveAttachment(123);
        await comp.$nextTick();

        // THEN
        expect(comp.attachment).toBe(foundAttachment);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAttachment = { id: 123 };
        attachmentServiceStub.find.resolves(foundAttachment);

        // WHEN
        comp.beforeRouteEnter({ params: { attachmentId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.attachment).toBe(foundAttachment);
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
