import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore

// prettier-ignore
const Project = () => import('@/entities/project/project.vue');
// prettier-ignore
const ProjectUpdate = () => import('@/entities/project/project-update.vue');
// prettier-ignore
const ProjectDetails = () => import('@/entities/project/project-details.vue');
// prettier-ignore
const Label = () => import('@/entities/label/label.vue');
// prettier-ignore
const LabelUpdate = () => import('@/entities/label/label-update.vue');
// prettier-ignore
const LabelDetails = () => import('@/entities/label/label-details.vue');
// prettier-ignore
const Ticket = () => import('@/entities/ticket/ticket.vue');
// prettier-ignore
const TicketUpdate = () => import('@/entities/ticket/ticket-update.vue');
// prettier-ignore
const TicketDetails = () => import('@/entities/ticket/ticket-details.vue');
// prettier-ignore
const Attachment = () => import('@/entities/attachment/attachment.vue');
// prettier-ignore
const AttachmentUpdate = () => import('@/entities/attachment/attachment-update.vue');
// prettier-ignore
const AttachmentDetails = () => import('@/entities/attachment/attachment-details.vue');
// prettier-ignore
const Comment = () => import('@/entities/comment/comment.vue');
// prettier-ignore
const CommentUpdate = () => import('@/entities/comment/comment-update.vue');
// prettier-ignore
const CommentDetails = () => import('@/entities/comment/comment-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default [
  {
    path: '/project',
    name: 'Project',
    component: Project,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/project/new',
    name: 'ProjectCreate',
    component: ProjectUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/project/:projectId/edit',
    name: 'ProjectEdit',
    component: ProjectUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/project/:projectId/view',
    name: 'ProjectView',
    component: ProjectDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/label',
    name: 'Label',
    component: Label,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/label/new',
    name: 'LabelCreate',
    component: LabelUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/label/:labelId/edit',
    name: 'LabelEdit',
    component: LabelUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/label/:labelId/view',
    name: 'LabelView',
    component: LabelDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/ticket',
    name: 'Ticket',
    component: Ticket,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/ticket/new',
    name: 'TicketCreate',
    component: TicketUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/ticket/:ticketId/edit',
    name: 'TicketEdit',
    component: TicketUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/ticket/:ticketId/view',
    name: 'TicketView',
    component: TicketDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/attachment',
    name: 'Attachment',
    component: Attachment,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/attachment/new',
    name: 'AttachmentCreate',
    component: AttachmentUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/attachment/:attachmentId/edit',
    name: 'AttachmentEdit',
    component: AttachmentUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/attachment/:attachmentId/view',
    name: 'AttachmentView',
    component: AttachmentDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/comment',
    name: 'Comment',
    component: Comment,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/comment/new',
    name: 'CommentCreate',
    component: CommentUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/comment/:commentId/edit',
    name: 'CommentEdit',
    component: CommentUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/comment/:commentId/view',
    name: 'CommentView',
    component: CommentDetails,
    meta: { authorities: [Authority.USER] },
  },
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
];
