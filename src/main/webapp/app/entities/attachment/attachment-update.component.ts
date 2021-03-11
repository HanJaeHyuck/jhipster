import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { required, minLength } from 'vuelidate/lib/validators';

import TicketService from '@/entities/ticket/ticket.service';
import { ITicket } from '@/shared/model/ticket.model';

import { IAttachment, Attachment } from '@/shared/model/attachment.model';
import AttachmentService from './attachment.service';

const validations: any = {
  attachment: {
    name: {
      required,
      minLength: minLength(3),
    },
    file: {},
  },
};

@Component({
  validations,
})
export default class AttachmentUpdate extends mixins(JhiDataUtils) {
  @Inject('attachmentService') private attachmentService: () => AttachmentService;
  public attachment: IAttachment = new Attachment();

  @Inject('ticketService') private ticketService: () => TicketService;

  public tickets: ITicket[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.attachmentId) {
        vm.retrieveAttachment(to.params.attachmentId);
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
  }

  public save(): void {
    this.isSaving = true;
    if (this.attachment.id) {
      this.attachmentService()
        .update(this.attachment)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Attachment is updated with identifier ' + param.id;
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.attachmentService()
        .create(this.attachment)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Attachment is created with identifier ' + param.id;
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

  public retrieveAttachment(attachmentId): void {
    this.attachmentService()
      .find(attachmentId)
      .then(res => {
        this.attachment = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.ticketService()
      .retrieve()
      .then(res => {
        this.tickets = res.data;
      });
  }
}
