/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ProjectDetailComponent from '@/entities/project/project-details.vue';
import ProjectClass from '@/entities/project/project-details.component';
import ProjectService from '@/entities/project/project.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Project Management Detail Component', () => {
    let wrapper: Wrapper<ProjectClass>;
    let comp: ProjectClass;
    let projectServiceStub: SinonStubbedInstance<ProjectService>;

    beforeEach(() => {
      projectServiceStub = sinon.createStubInstance<ProjectService>(ProjectService);

      wrapper = shallowMount<ProjectClass>(ProjectDetailComponent, {
        store,
        localVue,
        router,
        provide: { projectService: () => projectServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundProject = { id: 123 };
        projectServiceStub.find.resolves(foundProject);

        // WHEN
        comp.retrieveProject(123);
        await comp.$nextTick();

        // THEN
        expect(comp.project).toBe(foundProject);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundProject = { id: 123 };
        projectServiceStub.find.resolves(foundProject);

        // WHEN
        comp.beforeRouteEnter({ params: { projectId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.project).toBe(foundProject);
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
