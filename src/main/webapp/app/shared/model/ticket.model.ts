import { IAttachment } from '@/shared/model/attachment.model';
import { IProject } from '@/shared/model/project.model';
import { IUser } from '@/shared/model/user.model';
import { ILabel } from '@/shared/model/label.model';

import { Status } from '@/shared/model/enumerations/status.model';
import { Type } from '@/shared/model/enumerations/type.model';
import { Priority } from '@/shared/model/enumerations/priority.model';
export interface ITicket {
  id?: number;
  title?: string;
  description?: string | null;
  dueDate?: Date | null;
  date?: Date | null;
  status?: Status | null;
  type?: Type | null;
  priority?: Priority | null;
  attachments?: IAttachment[] | null;
  project?: IProject | null;
  assignedTo?: IUser | null;
  reportedBy?: IUser | null;
  labels?: ILabel[] | null;
}

export class Ticket implements ITicket {
  constructor(
    public id?: number,
    public title?: string,
    public description?: string | null,
    public dueDate?: Date | null,
    public date?: Date | null,
    public status?: Status | null,
    public type?: Type | null,
    public priority?: Priority | null,
    public attachments?: IAttachment[] | null,
    public project?: IProject | null,
    public assignedTo?: IUser | null,
    public reportedBy?: IUser | null,
    public labels?: ILabel[] | null
  ) {}
}
