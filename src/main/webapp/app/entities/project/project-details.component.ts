import { Component, Vue, Inject } from 'vue-property-decorator';

import { IProject } from '@/shared/model/project.model';
import ProjectService from './project.service';

@Component
export default class ProjectDetails extends Vue {
  @Inject('projectService') private projectService: () => ProjectService;
  public project: IProject = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.projectId) {
        vm.retrieveProject(to.params.projectId);
      }
    });
  }

  public retrieveProject(projectId) {
    this.projectService()
      .find(projectId)
      .then(res => {
        this.project = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
