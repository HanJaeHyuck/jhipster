import { Component, Vue, Inject } from 'vue-property-decorator';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import UserService from '@/admin/user-management/user-management.service';

import { IComment, Comment } from '@/shared/model/comment.model';
import CommentService from './comment.service';

const validations: any = {
  comment: {
    date: {},
    text: {},
  },
};

@Component({
  validations,
})
export default class CommentUpdate extends Vue {
  @Inject('commentService') private commentService: () => CommentService;
  public comment: IComment = new Comment();

  public comments: IComment[] = [];

  @Inject('userService') private userService: () => UserService;

  public users: Array<any> = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.commentId) {
        vm.retrieveComment(to.params.commentId);
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
    if (this.comment.id) {
      this.commentService()
        .update(this.comment)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Comment is updated with identifier ' + param.id;
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.commentService()
        .create(this.comment)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Comment is created with identifier ' + param.id;
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
      this.comment[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.comment[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.comment[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.comment[field] = null;
    }
  }

  public retrieveComment(commentId): void {
    this.commentService()
      .find(commentId)
      .then(res => {
        res.date = new Date(res.date);
        this.comment = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.commentService()
      .retrieve()
      .then(res => {
        this.comments = res.data;
      });
    this.userService()
      .retrieve()
      .then(res => {
        this.users = res.data;
      });
  }
}
