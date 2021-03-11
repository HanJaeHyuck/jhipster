<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="jh001App.comment.home.createOrEditLabel" data-cy="CommentCreateUpdateHeading">Create or edit a Comment</h2>
        <div>
          <div class="form-group" v-if="comment.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="comment.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="comment-date">Date</label>
            <div class="d-flex">
              <input
                id="comment-date"
                data-cy="date"
                type="datetime-local"
                class="form-control"
                name="date"
                :class="{ valid: !$v.comment.date.$invalid, invalid: $v.comment.date.$invalid }"
                :value="convertDateTimeFromServer($v.comment.date.$model)"
                @change="updateZonedDateTimeField('date', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="comment-text">Text</label>
            <input
              type="text"
              class="form-control"
              name="text"
              id="comment-text"
              data-cy="text"
              :class="{ valid: !$v.comment.text.$invalid, invalid: $v.comment.text.$invalid }"
              v-model="$v.comment.text.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="comment-login">Login</label>
            <select class="form-control" id="comment-login" data-cy="login" name="login" v-model="comment.login">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="comment.login && userOption.id === comment.login.id ? comment.login : userOption"
                v-for="userOption in users"
                :key="userOption.id"
              >
                {{ userOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="comment-child">Child</label>
            <select class="form-control" id="comment-child" data-cy="child" name="child" v-model="comment.child">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="comment.child && commentOption.id === comment.child.id ? comment.child : commentOption"
                v-for="commentOption in comments"
                :key="commentOption.id"
              >
                {{ commentOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.comment.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./comment-update.component.ts"></script>
