import { ITicket } from '@/shared/model/ticket.model';

export interface ILabel {
  id?: number;
  label?: string;
  tickets?: ITicket[] | null;
}

export class Label implements ILabel {
  constructor(public id?: number, public label?: string, public tickets?: ITicket[] | null) {}
}
