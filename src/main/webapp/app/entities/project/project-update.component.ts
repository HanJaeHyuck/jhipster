import { Component, Vue, Inject } from 'vue-property-decorator';

import { IProject, Project } from '@/shared/model/project.model';
import ProjectService from './project.service';

const validations: any = {
  project: {
    name: {},
  },
};

@Component({
  validations,
})
export default class ProjectUpdate extends Vue {
  @Inject('projectService') private projectService: () => ProjectService;
  public project: IProject = new Project();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.projectId) {
        vm.retrieveProject(to.params.projectId);
      }
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.project.id) {
      this.projectService()
        .update(this.project)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Project is updated with identifier ' + param.id;
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.projectService()
        .create(this.project)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Project is created with identifier ' + param.id;
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    }
  }

  public retrieveProject(projectId): void {
    this.projectService()
      .find(projectId)
      .then(res => {
        this.project = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
