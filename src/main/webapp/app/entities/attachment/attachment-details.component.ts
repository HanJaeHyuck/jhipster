import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { IAttachment } from '@/shared/model/attachment.model';
import AttachmentService from './attachment.service';

@Component
export default class AttachmentDetails extends mixins(JhiDataUtils) {
  @Inject('attachmentService') private attachmentService: () => AttachmentService;
  public attachment: IAttachment = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.attachmentId) {
        vm.retrieveAttachment(to.params.attachmentId);
      }
    });
  }

  public retrieveAttachment(attachmentId) {
    this.attachmentService()
      .find(attachmentId)
      .then(res => {
        this.attachment = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
