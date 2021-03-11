<template>
  <div>
    <h2 id="page-heading" data-cy="CommentHeading">
      <span id="comment-heading">Comments</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link
          :to="{ name: 'CommentCreate' }"
          tag="button"
          id="jh-create-entity"
          data-cy="entityCreateButton"
          class="btn btn-primary jh-create-entity create-comment"
        >
          <font-awesome-icon icon="plus"></font-awesome-icon>
          <span> Create a new Comment </span>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && comments && comments.length === 0">
      <span>No comments found</span>
    </div>
    <div class="table-responsive" v-if="comments && comments.length > 0">
      <table class="table table-striped" aria-describedby="comments">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('date')">
              <span>Date</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'date'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('text')">
              <span>Text</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'text'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('login.id')">
              <span>Login</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'login.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('child.id')">
              <span>Child</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'child.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="comment in comments" :key="comment.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CommentView', params: { commentId: comment.id } }">{{ comment.id }}</router-link>
            </td>
            <td>{{ comment.date | formatDate }}</td>
            <td>{{ comment.text }}</td>
            <td>
              {{ comment.login ? comment.login.id : '' }}
            </td>
            <td>
              <div v-if="comment.child">
                <router-link :to="{ name: 'CommentView', params: { commentId: comment.child.id } }">{{ comment.child.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'CommentView', params: { commentId: comment.id } }"
                  tag="button"
                  class="btn btn-info btn-sm details"
                  data-cy="entityDetailsButton"
                >
                  <font-awesome-icon icon="eye"></font-awesome-icon>
                  <span class="d-none d-md-inline">View</span>
                </router-link>
                <router-link
                  :to="{ name: 'CommentEdit', params: { commentId: comment.id } }"
                  tag="button"
                  class="btn btn-primary btn-sm edit"
                  data-cy="entityEditButton"
                >
                  <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                  <span class="d-none d-md-inline">Edit</span>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(comment)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="jh001App.comment.delete.question" data-cy="commentDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-comment-heading">Are you sure you want to delete this Comment?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-comment"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeComment()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./comment.component.ts"></script>
