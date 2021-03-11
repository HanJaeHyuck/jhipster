import { Component, Vue, Inject } from 'vue-property-decorator';

import { ITicket } from '@/shared/model/ticket.model';
import TicketService from './ticket.service';

@Component
export default class TicketDetails extends Vue {
  @Inject('ticketService') private ticketService: () => TicketService;
  public ticket: ITicket = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.ticketId) {
        vm.retrieveTicket(to.params.ticketId);
      }
    });
  }

  public retrieveTicket(ticketId) {
    this.ticketService()
      .find(ticketId)
      .then(res => {
        this.ticket = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
