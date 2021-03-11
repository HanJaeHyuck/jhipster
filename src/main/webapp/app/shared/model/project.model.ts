export interface IProject {
  id?: number;
  name?: string | null;
}

export class Project implements IProject {
  constructor(public id?: number, public name?: string | null) {}
}
