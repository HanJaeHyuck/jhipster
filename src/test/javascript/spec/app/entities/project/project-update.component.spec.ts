/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import ProjectUpdateComponent from '@/entities/project/project-update.vue';
import ProjectClass from '@/entities/project/project-update.component';
import ProjectService from '@/entities/project/project.service';

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
  describe('Project Management Update Component', () => {
    let wrapper: Wrapper<ProjectClass>;
    let comp: ProjectClass;
    let projectServiceStub: SinonStubbedInstance<ProjectService>;

    beforeEach(() => {
      projectServiceStub = sinon.createStubInstance<ProjectService>(ProjectService);

      wrapper = shallowMount<ProjectClass>(ProjectUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          projectService: () => projectServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.project = entity;
        projectServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(projectServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.project = entity;
        projectServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(projectServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundProject = { id: 123 };
        projectServiceStub.find.resolves(foundProject);
        projectServiceStub.retrieve.resolves([foundProject]);

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
