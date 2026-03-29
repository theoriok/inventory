#!/usr/bin/env bash

function cleanup_images() {
  if [ ! -z "$(docker images -f 'dangling=true' -q)" ]; then
     docker image prune -f
  fi
}

docker-compose down --remove-orphans
cleanup_images
docker-compose up
