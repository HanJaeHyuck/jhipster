import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AttachmentService from '@/entities/attachment/attachment.service';
import { IAttachment } from '@/shared/model/attachment.model';

import ProjectService from '@/entities/project/project.service';
import { IProject } from '@/shared/model/project.model';

import UserService from '@/admin/user-management/user-management.service';

import LabelService from '@/entities/label/label.service';
import { ILabel } from '@/shared/model/label.model';

import { ITicket, Ticket } from '@/shared/model/ticket.model';
import TicketService from './ticket.service';

const validations: any = {
  ticket: {
    title: {
      required,
    },
    description: {},
    dueDate: {},
    date: {},
    status: {},
    type: {},
    priority: {},
  },
};

@Component({
  validations,
})
export default class TicketUpdate extends Vue {
  @Inject('ticketService') private ticketService: () => TicketService;
  public ticket: ITicket = new Ticket();

  @Inject('attachmentService') private attachmentService: () => AttachmentService;

  public attachments: IAttachment[] = [];

  @Inject('projectService') private projectService: () => ProjectService;

  public projects: IProject[] = [];

  @Inject('userService') private userService: () => UserService;

  public users: Array<any> = [];

  @Inject('labelService') private labelService: () => LabelService;

  public labels: ILabel[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.ticketId) {
        vm.retrieveTicket(to.params.ticketId);
      }
      vm.initRelationships();
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
    this.ticket.labels = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.ticket.id) {
      this.ticketService()
        .update(this.ticket)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Ticket is updated with identifier ' + param.id;
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.ticketService()
        .create(this.ticket)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Ticket is created with identifier ' + param.id;
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

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.ticket[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.ticket[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.ticket[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.ticket[field] = null;
    }
  }

  public retrieveTicket(ticketId): void {
    this.ticketService()
      .find(ticketId)
      .then(res => {
        res.date = new Date(res.date);
        this.ticket = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.attachmentService()
      .retrieve()
      .then(res => {
        this.attachments = res.data;
      });
    this.projectService()
      .retrieve()
      .then(res => {
        this.projects = res.data;
      });
    this.userService()
      .retrieve()
      .then(res => {
        this.users = res.data;
      });
    this.labelService()
      .retrieve()
      .then(res => {
        this.labels = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
