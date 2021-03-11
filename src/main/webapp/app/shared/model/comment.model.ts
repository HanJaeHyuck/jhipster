import { IUser } from '@/shared/model/user.model';

export interface IComment {
  id?: number;
  date?: Date | null;
  text?: string | null;
  parents?: IComment[] | null;
  login?: IUser | null;
  child?: IComment | null;
}

export class Comment implements IComment {
  constructor(
    public id?: number,
    public date?: Date | null,
    public text?: string | null,
    public parents?: IComment[] | null,
    public login?: IUser | null,
    public child?: IComment | null
  ) {}
}
